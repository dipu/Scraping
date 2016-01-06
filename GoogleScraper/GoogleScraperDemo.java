
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

//Jaunt demo: searches for 'butterflies' at Google and prints urls of search results from first page.
class GoogleScraper extends Thread {

    private Thread t;
    private final String queryString;
    private final String threadName;

    GoogleScraper(String name, String query) {
        threadName = name;
        queryString = query;
        System.out.println("Creating " + threadName);
    }

    @Override
    public void run() {
        System.out.println("Running " + threadName);
        try {
            UserAgent userAgent = new UserAgent();      //create new userAgent (headless browser)
            userAgent.settings.autoSaveAsHTML = true;
            userAgent.visit("https://google.com.bd");       //visit google
            userAgent.doc.apply(queryString);         //apply form input (starting at first editable field)
            userAgent.doc.submit();      //click submit button labelled "Google Search"

            Elements links = userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>");   //find search result links 
            for (Element link : links) {
                System.out.println(link.getAt("href"));     //print results
            }
            for (int i = 4; i > 0; i--) {
                System.out.println("Thread: " + threadName + ", " + i);
                // Let the thread sleep for a while.
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadName + " interrupted.");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        System.out.println("Thread " + threadName + " exiting.");
    }

    @Override
    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

}

public class GoogleScraperDemo {

    public static void main(String args[]) throws JauntException {

        GoogleScraper T1 = new GoogleScraper("Thread-1","jubayer arefin");
        T1.start();

        GoogleScraper T2 = new GoogleScraper("Thread-2","butterflies");
        T2.start();
    }

}
