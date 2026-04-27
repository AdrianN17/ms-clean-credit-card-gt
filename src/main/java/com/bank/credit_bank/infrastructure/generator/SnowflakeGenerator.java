package com.bank.credit_bank.infrastructure.generator;

import com.bank.credit_bank.application.generator.port.out.IdGeneratePort;

import java.util.Optional;

public class SnowflakeGenerator implements IdGeneratePort {

    private final long machineId = 1L;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    private synchronized Long nextId() {

        long timestamp = System.currentTimeMillis();

        if (timestamp == lastTimestamp) {
            sequence++;
        } else {
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return (timestamp << 22)
                | (machineId << 12)
                | sequence;
    }

    @Override
    public Optional<Long> load() {
        return Optional.of(nextId());
    }
}
