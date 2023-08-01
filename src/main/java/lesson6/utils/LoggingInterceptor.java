package lesson6.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {

    @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();

        System.out.printf("Sending request %s on %s%n%s%n",
                request.url(), chain.connection(), request.headers());

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();

        System.out.printf("Received response for %s in %.1fms%n%s%n",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        return response;
    }
}
