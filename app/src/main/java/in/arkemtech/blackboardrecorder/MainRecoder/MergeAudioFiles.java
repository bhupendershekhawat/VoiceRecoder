//package in.arkemtech.blackboardrecorder;
//
//import android.content.Context;
//import android.graphics.Movie;
//import android.widget.Toast;
//
//import org.mp4parser.Container;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.SequenceInputStream;
//import java.nio.channels.Channels;
//import java.nio.channels.WritableByteChannel;
//
//public class MergeAudioFiles {
//
//    String fistream1_URL;
//    String fistream2_URL;
//    String final_Url;
//    Context context;
//
//
//    public MergeAudioFiles(Context context, String fistream1_URL, String fistream2_URL, String final_Url) {
//        this.fistream1_URL = fistream1_URL;
//        this.fistream2_URL = fistream2_URL;
//        this.final_Url = final_Url;
//        this.context = context;
//
//    }
//
//
//    public static void append(
//            final String firstFile,
//            final String secondFile,
//
//            final String newFile) throws IOException {
//        final Movie movieA = MovieCreator.build(new FileDataSourceImpl(secondFile));
//        final Movie movieB = MovieCreator.build(new FileDataSourceImpl(firstFile));
//
//        final Movie finalMovie = new Movie();
//
//        final List<Track> movieOneTracks = movieA.getTracks();
//        final List<Track> movieTwoTracks = movieB.getTracks();
//
//        for (int i = 0; i < movieOneTracks.size() || i < movieTwoTracks.size(); ++i) {
//            finalMovie.addTrack(new AppendTrack(movieTwoTracks.get(i), movieOneTracks.get(i)));
//        }
//
//        final Container container = new DefaultMp4Builder().build(finalMovie);
//
//        final FileOutputStream fos = new FileOutputStream(new File(String.format(newFile)));
//        final WritableByteChannel bb = Channels.newChannel(fos);
//        container.writeContainer(bb);
//        fos.close();
//    }
//
//    public static boolean concatTwoVideos(File src1, File src2, File dst) {
//        try {
//            FileDataSourceImpl file1 = new FileDataSourceImpl(src1);
//            FileDataSourceImpl file2 = new FileDataSourceImpl(src2);
//            Movie result = new Movie();
//            Movie movie1 = MovieCreator.build(file1);
//            Movie movie2 = MovieCreator.build(file2);
//
//            Movie[] inMovies = new Movie[]{
//                    movie1, movie2
//            };
//
//            List<Track> videoTracks = new LinkedList<Track>();
//            List<Track> audioTracks = new LinkedList<Track>();
//
//            for (Movie m : inMovies) {
//                for (Track t : m.getTracks()) {
//                    if (t.getHandler().equals("soun")) {
//                        audioTracks.add(t);
//                    }
//                    if (t.getHandler().equals("vide")) {
//                        videoTracks.add(t);
//                    }
//                }
//            }
//
//            if (audioTracks.size() > 0) {
//
//                result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
//
//            }
//            if (videoTracks.size() > 0) {
//
//                result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
//
//            }
//
//            Container out = new DefaultMp4Builder().build(result);
//            MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
//            mvhd.setMatrix(Matrix.ROTATE_180);
//            if (!dst.exists()) {
//                dst.createNewFile();
//            }
//            FileOutputStream fos = new FileOutputStream(dst);
//            WritableByteChannel fc = fos.getChannel();
//            try {
//                out.writeContainer(fc);
//            } finally {
//                fc.close();
//                fos.close();
//                file1.close();
//                file2.close();
//            }
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//}
