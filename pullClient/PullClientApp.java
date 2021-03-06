import java.util.Timer;
import java.util.ArrayList;
import java.util.Map;


public class PullClientApp implements PullWorkerListener
{
	private static final String TAG = "APP";

	private Timer timer;
	private PullWorker pullWorker;

	// for scheduling PullWorker thread
	private final static int DELAY = 1000;
	private final static int PERIOD = 5000;

	private final static String REMOTE_SERVER = "127.0.0.1";
	private final static int REMOTE_PORT = 6004;

	// keeps the PullWorker status
	private String pullWorkerStatus;

	private Pull pull;


	// constructor
	public PullClientApp()
	{
    		timer = new Timer();
	}


	/*
	 * calls back when PullWorker status changed
	 */
	@Override
	public void onPullWorkerStatusChanged(String status)
	{
		pullWorkerStatus = status;

		System.out.println(PullWorkerListener.TAG + " : " + pullWorkerStatus);
	}


	/*
	 * calls back when PullWorker has received a Pull
	 */
	@Override
	public void onPullReceived(Pull pull)
	{
		if(pull != null)
		{
			this.pull = pull;

			Map<String, ArrayList<Report>> machinesReports = this.pull.getMachinesReports();

			// get keys list
			ArrayList<String> keys = new ArrayList<String>();
			keys.addAll( machinesReports.keySet() );

			for(int i=0; i<keys.size(); i++)
			{
				String machine = keys.get(i);

				System.out.println(machine);

				ArrayList<Report> machineReportList = machinesReports.get(machine);

				System.out.println(machineReportList.size());
			}
		}
	}


	/*
	 * set PullWorker as a scheduled thread with DELAY and PERIOD
	 */
	private void startPullWorker()
	{
		pullWorker = new PullWorker(this, REMOTE_SERVER, REMOTE_PORT);

		timer.schedule(pullWorker, DELAY, PERIOD);
	}


	public static void main(String[] args)
	{
		PullClientApp pullclientApp = new PullClientApp();

		pullclientApp.startPullWorker();
  	}
}
