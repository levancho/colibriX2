package com.despani.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory that allows threads to be named.
 *
 * An instance will either create new Threads, or use use a delegate Thread Factory.
 * When a new thread is generated, the name of the generated thread is replaced by concatenation of the provided thread
 * name prefix (which is an other argument of the constructor) and a sequence number. Sequence numbers are guaranteed to
 * be unique for threads generated by the same instance of this class.
 *
 * Optionally, this implementation allows the priority, isDaemon and threadGroup and stackSize value of the generated
 * threads to be set/overridden. Note that the threadGroup and stackSize value cannot be overridden when a delegate
 * thread factory is used.
 *
 * This implementation is thread safe when the provided delegate is thread safe.
 *
 * @author Guus der Kinderen, guus.der.kinderen@gmail.com
 */
public class NamedThreadFactory implements ThreadFactory
{
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String threadNamePrefix;
    private final ThreadFactory delegate;
    private final Boolean daemon;
    private final Integer priority;
    private final Long stackSize;
    private final ThreadGroup threadGroup;

    /**
     * Constructs an instance that delegates thread creation to the thread factory passed as an argument. When the
     * delegate argument is null, the instance will instantiate threads itself (similar to the functionality of the
     * other constructor of this class).
     *
     * When null is provided for the optional arguments of this method, the values as defined by the delegate factory
     * are used.
     *
     * @param threadNamePrefix The prefix of the name for new threads (cannot be null or an empty string).
     * @param delegate The factory to which this implementation delegates to (null when no override is desired).
     * @param daemon override for the isDaemon value for new threads (null when no override is desired).
     * @param priority override for the priority value for new threads (null when no override is desired).
     */
    public NamedThreadFactory(String threadNamePrefix, ThreadFactory delegate, Boolean daemon, Integer priority )
    {
        if ( threadNamePrefix == null || threadNamePrefix.isEmpty() )
        {
            throw new IllegalArgumentException( "Argument 'threadNamePrefix' cannot be null or an empty string." );
        }
        this.threadNamePrefix = threadNamePrefix;
        this.delegate = delegate;
        this.daemon = daemon;
        this.priority = priority;
        this.threadGroup = null;
        this.stackSize = null;
    }

    /**
     * Constructs a thread factory that will create new Thread instances (as opposed to using a delegate thread
     * factory).
     *
     * When null is provided for the optional arguments of this method, default values as provided by the Thread class
     * implementation will be used.
     *
     * @param threadNamePrefix The prefix of the name for new threads (cannot be null or an empty string).
     * @param daemon the isDaemon value for new threads (null to use default value).
     * @param priority override for the priority value for new threads (null to use default value).
     * @param threadGroup override for the thread group (null to use default value).
     * @param stackSize override for the stackSize value for new threads (null to use default value).
     */
    public NamedThreadFactory(String threadNamePrefix, Boolean daemon, Integer priority, ThreadGroup threadGroup, Long stackSize )
    {
        if ( threadNamePrefix == null || threadNamePrefix.isEmpty() )
        {
            throw new IllegalArgumentException( "Argument 'threadNamePrefix' cannot be null or an empty string." );
        }

        this.delegate = null;
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
        this.priority = priority;
        this.threadGroup = threadGroup;
        this.stackSize = stackSize;
    }

    @Override
    public Thread newThread( Runnable runnable )
    {
        final String name = threadNamePrefix + threadNumber.incrementAndGet();

        final Thread thread;
        if ( delegate != null )
        {
            thread = delegate.newThread( runnable );
            thread.setName( name );
        }
        else
        {
            if ( stackSize != null )
            {
                thread = new Thread( threadGroup, runnable, name, stackSize );
            }
            else
            {
                thread = new Thread( threadGroup, runnable, name );
            }
        }

        if ( daemon != null && thread.isDaemon() != daemon )
        {
            thread.setDaemon( daemon );
        }

        if ( priority != null && thread.getPriority() != priority )
        {
            thread.setPriority( priority );
        }

        return thread;
    }
}
