package testSuite.in_progress.stack_overflow.mediaRecord;

public class Test {
    public static void test( boolean isChecked) {
        MediaRecorder recorder = new MediaRecorder();

        recorder.setAudioSource();
        recorder.setOutputFormat();
        recorder.setAudioEncoder();
        recorder.setOutputFile();

        while (isChecked) { // While loop will make it start and stop multiple times
            recorder.start(); // here it were it throws
            //...
            recorder.stop();
        }

    }


    public static void test2( boolean isChecked) {
        MediaRecorder recorder = new MediaRecorder();

        recorder.setAudioSource();
        recorder.setVideoSource();
        recorder.setOutputFormat();
        recorder.setProfile(); // setProfile error
        // From stackoverflow - setProfile() tries to setOutput but cannot because it is already explicitly set - which makes it redundant
        // From documentation - setProfile() should be done after setAudioSource and setVideoSource and after should be setOutputFile - however, it does not appear in the SM    

    }
}
