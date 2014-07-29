package com.example.helloworld.resources

import com.google.common.base.Optional
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput
import java.util.concurrent.atomic.AtomicLong

@Path("/sounds")
public class SoundResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SoundResource.class);

    private final AtomicLong counter;

    public SoundResource() {
        this.counter = new AtomicLong();
    }

//    @GET
//    @Path("{sound}.wav")
//    @Produces("audio/x-wav")
//    public Response getSound(@PathParam("sound") Optional<String> sound) {
//        println "getSound sound=${sound.orNull()}"
//
////        def streamingOutput = new StreamingOutput() {
////            public void write(OutputStream output) throws IOException, WebApplicationException {
////                try {
////                    def soundFile = new File("sounds/${sound}.wav")
////                    soundFile.withInputStream { is -> output << is }
////                } catch (Exception e) {
////                    System.out.println("Error getting sound ${sound}.")
////                    e.printStackTrace()
////                    throw new WebApplicationException(e);
////                }
////            }
////        };
//
//        def file = new File("sounds/${sound}.wav")
//        Response response = Response
//                    .ok()
////                    .cacheControl(cc)
////                    .tag(etag)
////                    .lastModified(updateTimestamp)
////                    .expires(expirationTimestamp)
////                    .type("image/png")
//                    .entity(file)
//                    .build();
//            return response;
//    }

    @GET
    @Path("play")
    @Produces(MediaType.APPLICATION_JSON)
    public String playSound(@QueryParam("sound") Optional<String> sound) {
        println "sound=${sound.orNull()}"

        try {
            playSound(sound.orNull())
            return "Playing ${sound.orNull()}"
        } catch (Exception ex) {
            System.out.println("Error playing sound.")
            ex.printStackTrace()
            return "Error playing ${sound.orNull()}"
        }
    }

    public void playSound(String sound = "alarm1") {
        def soundFile = new File("sounds/${sound}.wav")
        println "playing ${soundFile.absolutePath}"

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile)
        Clip clip = AudioSystem.getClip()
        clip.open(audioInputStream)
        clip.start()
    }
}