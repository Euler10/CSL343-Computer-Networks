
package csl343.receiverapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


abstract class AbstractRecorder implements Recorder {
  protected final PullTransport pullTransport;
  protected final File file;
  private final OutputStream outputStream;

  protected AbstractRecorder(PullTransport pullTransport, File file) {
    this.pullTransport = pullTransport;
    this.file = file;
    this.outputStream = outputStream(file);
  }

  @Override public void startRecording() {
    new Thread(new Runnable() {
      @Override public void run() {
        try {
          pullTransport.start(outputStream);
        } catch (IOException e) {
          new RuntimeException(e);
        }
      }
    }).start();
  }

  private OutputStream outputStream(File file) {
    if (file == null) throw new RuntimeException("file is null !");
    OutputStream outputStream;
    try {
      outputStream = new FileOutputStream(file);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("could not build OutputStream from" +
          " this file" + file.getName(), e);
    }
    return outputStream;
  }

  @Override public void stopRecording() {
    pullTransport.stop();
  }

  @Override public void pauseRecording() {
    pullTransport.source().isEnableToBePulled(false);
  }

  @Override public void resumeRecording() {
    pullTransport.source().isEnableToBePulled(true);
    startRecording();
  }
}