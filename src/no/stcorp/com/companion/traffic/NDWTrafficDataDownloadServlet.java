package no.stcorp.com.companion.traffic;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import no.stcorp.com.companion.util.CommonProperties;

/**
 * Servlet implementation class NDWTrafficDataDownloadServlet
 */
@WebServlet("/NDWTrafficDataDownloadServlet")
public class NDWTrafficDataDownloadServlet extends HttpServlet {
  private static final long serialVersionUID = 2006357242997698560L;

  private Date mStartDate;
  private static final Logger mLogger = Logger.getLogger(NDWTrafficDataDownloadServlet.class);
  private CommonProperties mProperties = null;
  // private ScheduledExecutorService mScheduler;
  private String mVersionNumber = null;
  private String mVersionDate = null;

  /**
   * @see HttpServlet#HttpServlet()
   */
  public NDWTrafficDataDownloadServlet() {
    super();
    mStartDate = new Date();
    mProperties = CommonProperties.getInstance();
  }

  /**
   * @see Servlet#init(ServletConfig)
   */
  public void init(ServletConfig config) throws ServletException {
    mStartDate = new Date();
    try {
      // This properties file is loaded for all classes at this place
      Properties prop = mProperties.getProperties();
      prop.load(config.getServletContext().getResourceAsStream("/WEB-INF/resources/config.properties"));
      // String logPath = (String) prop.get("logPath");
      mLogger.info("The properties of the servlet at startup");
      mLogger.info(prop.toString());
      while (config.getInitParameterNames().hasMoreElements()) {
        mLogger.info(config.getInitParameterNames().nextElement());
      }
      mVersionNumber = (String) prop.get("versionNumber");
      mVersionDate = (String) prop.get("versionDate");
      // mLogger.log(Level.INFO, "Starting NDW Traffic Download Servlet", "");
      // mScheduler = Executors.newSingleThreadScheduledExecutor();
      // // Every 1 minute
      // mScheduler.scheduleAtFixedRate(new NDWDownloader(), 10, 60, TimeUnit.SECONDS);
    } catch (IOException ex) {
      mLogger.error("Something went wrong loading the properties file" + ex);
    } catch (Exception ex) {
      mLogger.error("Something else went wrong while reading the properties file" + ex);
    }

  }

  /**
   * @return the current time as a string
   * @param pTime
   *          The time to return as a string
   */
  public static String getCurrentTimeAsString(Date pTime) {
    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    return sdfDate.format(pTime);
  }

  /**
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    mLogger.info("Doing get request");
    response.setContentType("text/html");
    // Set refresh, auto reload time as 20 seconds
    response.setIntHeader("Refresh", 60);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head>");
    out.println("<style>");
    out.println("body {background-color: rgb(20,0,0)}");
    out.println("h3   {color:yellow}");
    out.println("table, th, td {");
    out.println("border: 1px solid white;");
    out.println("border-collapse: collapse;");
    out.println("color: white;");
    out.println("}");
    out.println("th,td {");
    out.println("padding: 15px;");
    out.println("}");
    out.println("</style>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h3>COMPANION NDW Traffic Data Download Servlet (version " + mVersionNumber + " dated " + mVersionDate
        + ") running since " + getCurrentTimeAsString(mStartDate) + "</h3>");
    out.println("<table style=\"width:100%\"><tr>");
    out.println("<th>Time</th>");
    out.println("<th>Message / Last request</th>");
    out.println("<th>Number of requests</th>");
    out.println("</tr>");
    out.println("<tr>");
    out.println("<td>");
    out.println(getCurrentTimeAsString(new Date()));
    out.println("</td>");
    out.println("<td>");
    out.println("Servlet downloading NDW traffic data");
    out.println("</td>");
    out.println("<td>");
    out.println("To be filled");
    out.println("</td>");
    out.println("</tr>");
    out.println("</table>");
    out.println("</body>");
    out.println("</html>");
    out.close();
  }

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.servlet.GenericServlet#destroy()
   */
  @Override
  public void destroy() {
    // mScheduler.shutdownNow();
    mLogger.info("Servlet shutting down now");
  }

}
