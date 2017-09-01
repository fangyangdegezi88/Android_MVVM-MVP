package com.focusteach.android.record.util.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <先进先出队列实现>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/11]
 * @see [相关类/方法]
 * @since [V1]
 */
public class FIFOImpl<T> extends LinkedList<T> implements FIFO<T> {

    private int maxSize = Integer.MAX_VALUE;
    private final Object synObj = new Object();

    public FIFOImpl() {
        super();
    }

    public FIFOImpl(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    @Override
    public T addLastSafe(T addLast) {
        synchronized (synObj) {
            T head = null;
            while (size() >= maxSize) {
                head = poll();
            }
            addLast(addLast);
            return head;
        }
    }

    @Override
    public T pollSafe() {
        synchronized (synObj) {
            return poll();
        }
    }

    @Override
    public List<T> setMaxSize(int maxSize) {
        List<T> list = null;
        if (maxSize < this.maxSize) {
            list = new ArrayList<T>();
            synchronized (synObj) {
                while (size() > maxSize) {
                    list.add(poll());
                }
            }
        }
        this.maxSize = maxSize;
        return list;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }
}
