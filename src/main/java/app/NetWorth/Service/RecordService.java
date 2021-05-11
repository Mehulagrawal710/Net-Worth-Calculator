package app.NetWorth.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.NetWorth.Entity.NetWorthTarget;
import app.NetWorth.Entity.NetWorthTimeSeries;
import app.NetWorth.Entity.Section;
import app.NetWorth.Entity.User;
import app.NetWorth.Repository.NetWorthTimeSeriesRepository;
import app.NetWorth.Repository.SectionRepository;
import app.NetWorth.Repository.UserRepository;

@Service
public class RecordService {

	@Autowired
	UserRepository userRepository;
	@Autowired
	SectionRepository sectionRepository;
	@Autowired
	NetWorthTimeSeriesRepository netWorthTimeSeriesRepository;

	public void generateDefaultSections(User user) {
		String userId = user.getId();
		ArrayList<Section> defaultSections = new ArrayList<>();
		// Asset Sections
		defaultSections.add(new Section("Cash Holdings", "asset", 0, userId, true));
		defaultSections.add(new Section("Stock Investments", "asset", 0, userId, true));
		defaultSections.add(new Section("Real Estate", "asset", 0, userId, true));
		defaultSections.add(new Section("Automobiles", "asset", 0, userId, true));
		// Liability Section
		defaultSections.add(new Section("Mortgages", "liability", 0, userId, true));

		sectionRepository.saveAll(defaultSections);
	}

	public void initiateNetWorthTimeSeries(User user) {
		String userId = user.getId();
		NetWorthTimeSeries series = new NetWorthTimeSeries(userId, new ArrayList<Date>(), new ArrayList<Integer>());
		netWorthTimeSeriesRepository.save(series);
	}

	public void updateNetWorthTimeSeries(String username, String entryType, Date entryTimeStamp, int amount) {
		User user = userRepository.findOneByUsername(username);
		NetWorthTimeSeries series = netWorthTimeSeriesRepository.findOneByBelongsToUserId(user.getId());
		series.getTimeCheckPoint().add(entryTimeStamp);
		if (series.getDataCheckPoint().isEmpty()) {
			series.getDataCheckPoint().add(amount);
		} else {
			if (entryType.equals("asset")) {
				int newNetWorth = series.getDataCheckPoint().get(series.getDataCheckPoint().size() - 1) + amount;
				series.getDataCheckPoint().add(newNetWorth);
			} else {
				int newNetWorth = series.getDataCheckPoint().get(series.getDataCheckPoint().size() - 1) - amount;
				series.getDataCheckPoint().add(newNetWorth);
			}
		}
		netWorthTimeSeriesRepository.save(series);
	}

	public ArrayList<Integer> getTargetCheckpoint(NetWorthTarget netWorthTarget, NetWorthTimeSeries netWorthTimeSeries,
			ArrayList<Date> extrapolatedLabelDate) {
		ArrayList<Integer> targetCheckpoint = new ArrayList<Integer>();

		ArrayList<Date> timeCheckpoint = netWorthTimeSeries.getTimeCheckPoint();
		timeCheckpoint.addAll(extrapolatedLabelDate);
		int length = timeCheckpoint.size();

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		if (netWorthTarget.getTargetType().equals("fixed")) {
			int fixedValue = netWorthTarget.getFixedValue();
			String targetCreatedOn = formatter.format(netWorthTarget.getTargetCreatedOn());
			for (int i = 0; i < length; i++) {
				String labelDate = formatter.format(timeCheckpoint.get(i));
				if (labelDate.compareTo(targetCreatedOn) < 0) {
					targetCheckpoint.add(null);
				} else {
					targetCheckpoint.add(fixedValue);
				}
			}
		} else {
			int initialValue = netWorthTarget.getInitialValue();
			float increaseRate = netWorthTarget.getIncreaseRate();
			int interval = netWorthTarget.getInterval();
			String targetCreatedOn = formatter.format(netWorthTarget.getTargetCreatedOn());
			for (int i = 0; i < length; i++) {
				String labelDate = formatter.format(timeCheckpoint.get(i));
				if (labelDate.compareTo(targetCreatedOn) < 0) {
					targetCheckpoint.add(null);
				} else {
					int targetForThisLabelDate = getRecurringTargetForLabelDate(labelDate, targetCreatedOn, interval,
							initialValue, increaseRate);
					targetCheckpoint.add(targetForThisLabelDate);
				}
			}
		}
		return targetCheckpoint;
	}

	private int getRecurringTargetForLabelDate(String labelDate, String targetCreatedOn, int interval, int initialValue,
			float increaseRate) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(labelDate);
			d2 = sdf.parse(targetCreatedOn);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date diff = new Date(d1.getTime() - d2.getTime());
		int duration = diff.getDate() - 1;

		int timesIncremented = duration / interval;
		float percent = (float) (increaseRate / 100.0);
		float increaseFactor = (float) Math.pow((1.0 + percent), timesIncremented);
		int targetAtLabelDate = (int) (initialValue * increaseFactor);

		return targetAtLabelDate;
	}

}
