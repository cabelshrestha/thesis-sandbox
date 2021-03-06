import org.jcsp.lang.*;
import org.jcsp.util.*;
import org.jcsp.awt.*;
import java.awt.*;

public class ActiveTextAreaExample {

  public static void main (String argv[]) {

    final ActiveClosingFrame frame =
      new ActiveClosingFrame ("ActiveTextAreaExample Example");

    final Any2OneChannel event = Channel.any2one (new OverWriteOldestBuffer (10));

    final String[] string =
      {"Entia Non Sunt Multiplicanda Praeter Necessitatem",
       "Everything we do, we do it to you",
       "Race Hazards - What Rice Hozzers?",
       "Cogito Ergo Occam"};

    final String goodbye = "Goodbye World";

    final ActiveTextArea[] activeText =
      new ActiveTextArea[string.length];

    for (int i = 0; i < string.length; i++) {
      activeText[i] = new ActiveTextArea (null, event.out (), string[i], 5, 40);
    }

    Panel panel = new Panel (new GridLayout (string.length/2, 2));
    for (int i = 0; i < string.length; i++) {
      panel.add (activeText[i]);
    }

    final Frame realFrame = frame.getActiveFrame ();
    realFrame.setBackground (Color.green);
    realFrame.add (panel);
    realFrame.pack ();
    realFrame.setVisible (true);

    new Parallel (
      new CSProcess[] {
        frame,
        new Parallel (activeText),
        new CSProcess () {
          public void run () {
            boolean running = true;
            while (running) {
              String s = (String) event.in ().read ();
              System.out.println (s);
              running = (! s.equals (goodbye));
            }
            realFrame.setVisible (false);
            System.exit (0);
          }
        }
      }
    ).run ();
  }

}
