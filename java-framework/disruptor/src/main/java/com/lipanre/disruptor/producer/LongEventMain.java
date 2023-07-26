package com.lipanre.disruptor.producer;

import com.lipanre.disruptor.event.LongEvent;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain
{
    public static void main(String[] args) throws Exception
    {
        int bufferSize = 1024; 

        Disruptor<LongEvent> disruptor =
                new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE);

        disruptor.handleEventsWith((event, sequence, endOfBatch) ->
                System.out.println("Event: " + event));
        disruptor.start();


        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
                ByteBuffer bb = ByteBuffer.allocate(8);
                for (long l = 0; true; l++)
                {
                    bb.putLong(0, l);
                    ringBuffer.publishEvent((event, sequence, buffer) -> event.set(buffer.getLong(0)), bb);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, "thred-" + i).start();
        }

    }
}