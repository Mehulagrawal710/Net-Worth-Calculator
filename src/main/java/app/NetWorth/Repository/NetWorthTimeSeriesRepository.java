package app.NetWorth.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import app.NetWorth.Entity.NetWorthTimeSeries;

public interface NetWorthTimeSeriesRepository extends MongoRepository<NetWorthTimeSeries, String> {

	NetWorthTimeSeries findOneByBelongsToUserId(String userId);

}
