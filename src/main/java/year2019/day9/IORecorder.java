package year2019.day9;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class IORecorder {

  private final Deque<Long> ioValues;
  private final Deque<CompletableFuture<Long>> ioRequests;
  private final ReentrantLock reentrantLock;

  public IORecorder() {
    this.ioValues = new LinkedList<>();
    this.ioRequests = new LinkedList<>();
    this.reentrantLock = new ReentrantLock(true);
  }

  public void record(long ioValue) {
    reentrantLock.lock();

    try {
      CompletableFuture<Long> availableIOValue = ioRequests.poll();
      if (availableIOValue == null) {
        ioValues.add(ioValue);
      } else {
        availableIOValue.complete(ioValue);
      }
    } finally {
      reentrantLock.unlock();
    }
  }

  public Future<Long> requestIOValue() {
    reentrantLock.lock();

    CompletableFuture<Long> future;
    try {
      Long availableIOValue = ioValues.poll();
      if (availableIOValue == null) {
        future = new CompletableFuture<>();
        ioRequests.add(future);
      } else {
        future = CompletableFuture.completedFuture(availableIOValue);
      }
    } finally {
      reentrantLock.unlock();
    }

    return future;
  }

}
