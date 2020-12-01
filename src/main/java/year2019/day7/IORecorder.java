package year2019.day7;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class IORecorder {

  private final Deque<Integer> ioValues;
  private final Deque<CompletableFuture<Integer>> ioRequests;
  private final ReentrantLock reentrantLock;

  public IORecorder() {
    this.ioValues = new LinkedList<>();
    this.ioRequests = new LinkedList<>();
    this.reentrantLock = new ReentrantLock(true);
  }

  public void record(int ioValue) {
    reentrantLock.lock();

    try {
      CompletableFuture<Integer> availableIOValue = ioRequests.poll();
      if (availableIOValue == null) {
        ioValues.add(ioValue);
      } else {
        availableIOValue.complete(ioValue);
      }
    } finally {
      reentrantLock.unlock();
    }
  }

  public Future<Integer> requestIOValue() {
    reentrantLock.lock();

    CompletableFuture<Integer> future;
    try {
      Integer availableIOValue = ioValues.poll();
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
