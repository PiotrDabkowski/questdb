package com.nfsdb.collections;

import com.nfsdb.lang.cst.Record;
import com.nfsdb.lang.cst.RecordMetadata;
import com.nfsdb.lang.cst.RecordSource;
import com.nfsdb.utils.Unsafe;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

public class DirectRecordLinkedList implements Closeable, RecordSource<Record> {
    private final RecordMetadata recordMetadata;
    private final DirectPagedBuffer buffer;
    private final DirectRecord bufferRecord;
    private long readOffset = -1;

    public DirectRecordLinkedList(RecordMetadata recordMetadata, long recordCount, long avgRecSize) {
        this.recordMetadata = recordMetadata;
        this.buffer = new DirectPagedBuffer(
                (long)(Math.ceil(recordCount * avgRecSize / 2.0 / AbstractDirectList.CACHE_LINE_SIZE)) * AbstractDirectList.CACHE_LINE_SIZE);
        bufferRecord = new DirectRecord(recordMetadata, buffer);
    }

    public long append(Record record, long prevRecordOffset) {
        long recordAddressBegin = buffer.getWriteOffsetQuick(8 + bufferRecord.getFixedBlockLength());
        Unsafe.getUnsafe().putLong(buffer.toAddress(recordAddressBegin), prevRecordOffset);
        bufferRecord.write(record, recordAddressBegin + 8);
        return recordAddressBegin;
    }

    public void init(long offset) {
        this.readOffset = offset;
    }

    @Override
    public void close() throws IOException {
        free();
    }

    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }

    private void free() throws IOException {
        buffer.close();
    }

    @Override
    public RecordMetadata getMetadata() {
        return recordMetadata;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Record> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return readOffset >= 0;
    }

    @Override
    public Record next() {
        bufferRecord.init(readOffset + 8);
        readOffset = Unsafe.getUnsafe().getLong(buffer.toAddress(readOffset));
        return bufferRecord;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}