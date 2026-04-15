package com.easyconnect.easyconnectap.errorhandling;

import com.easyconnect.easyconnectap.util.HttpStatusCodes;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * RxJava retry handler that automatically retries HTTP requests on 401 (Unauthorized)
 * errors up to {@link #maxRetries} times.
 *
 * <p>Designed to be used with RxJava's {@code retryWhen} operator on {@link io.reactivex.Single}
 * streams. When a {@link HttpException} with status 401 is received, the request is
 * retried (allowing the app to prompt for a passphrase and resend). All other errors
 * are immediately propagated.
 *
 * <p>Usage:
 * <pre>{@code
 * dppService.sendDPPUri(json)
 *     .retryWhen(new HttpErrorRetryChecker())
 *     .subscribe(...);
 * }</pre>
 *
 * @see HttpStatusCodes#HTTP_UNAUTHORIZED
 */
public class HttpErrorRetryChecker implements Function<Flowable<? extends Throwable>, Publisher<?>> {

    /** Default retry count constant (not actively used; see {@link #maxRetries}). */
    public static final int DEFAULT_RETRY_COUNT = 2;

    /** Maximum number of retry attempts for 401 errors. */
    private final int maxRetries = 3;
    private int retryCount = 0;

    /**
     * * Public empty constructor for HttpErrorRetryChecker
     */
    public  HttpErrorRetryChecker() {

    }

    /**
     * Evaluates each error emitted by the upstream and decides whether to retry.
     *
     * <p>Retries (emits a signal to resubscribe) if:
     * <ul>
     *   <li>The error is an {@link HttpException} with status 401</li>
     *   <li>The retry count has not exceeded {@link #maxRetries}</li>
     * </ul>
     * Otherwise, the original error is propagated downstream.
     *
     * @param flowable the error stream from the upstream observable
     * @return a publisher that either signals retry or propagates the error
     */
    @Override
    public Publisher<?> apply(Flowable<? extends Throwable> flowable) throws Exception {
        return flowable.flatMap((Function<Throwable, Publisher<?>>) throwable -> {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                // Retry on 401 (Unauthorized) — configurator may require passphrase
                if (httpException.code() == HttpStatusCodes.HTTP_UNAUTHORIZED && retryCount < maxRetries) {
                    retryCount++;
                    return Flowable.just(true);
                }
            }
            return Flowable.error(throwable);
        });
    }
}
