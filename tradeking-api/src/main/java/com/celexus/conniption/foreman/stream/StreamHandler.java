package com.celexus.conniption.foreman.stream;

/**
 * An interface to handle stream, mostly used to print the stream or log the result for now.
 * @author khoa
 *
 * @param <T>
 */
public interface StreamHandler<T> {
    public void handle(T t);
}
