package com.leon.experimental;

import org.asynchttpclient.*;

import java.io.FileOutputStream;
import java.util.concurrent.Executors;

public class AsyncFileDownloader {

    public static void main(String[] args) {
        try {
            FileOutputStream stream = new FileOutputStream("/tmp/data.osm.pbf");
            AsyncHttpClient client = Dsl.asyncHttpClient(Dsl.config().setRequestTimeout(5 * 60 * 60 * 1000));
            ListenableFuture<FileOutputStream> result = client.prepareGet("http://download.geofabrik.de/europe/lithuania-latest.osm.pbf")
                    .execute(new AsyncCompletionHandler<>() {

                        @Override
                        public AsyncHandler.State onBodyPartReceived(HttpResponseBodyPart content) throws Exception {
                            stream.getChannel().write(content.getBodyByteBuffer());
                            return AsyncHandler.State.CONTINUE;
                        }

                        @Override
                        public FileOutputStream onCompleted(Response response) {
                            return stream;
                        }

                    });

            result.addListener(() -> {
                try {
                    FileOutputStream osmData = result.get();
                    System.out.println(osmData.getFD());
                    stream.getChannel().close();
                    client.close();
                } catch (Exception ex) {
                    System.out.println("Error: " + ex);
                }
            }, Executors.newSingleThreadExecutor());
        } catch (Exception ex) {
            System.out.println("Error: " + ex);
        }
    }

}
