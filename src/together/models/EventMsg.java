/*
 * Created on 2012-2-13
 *
 * TODO To describe a star
 * Window - Preferences - Java - Code Style - Code Templates
 */
package together.models;

public class EventMsg {
	private String eid;
	private String place;
	private String uid;
	private String type;
	private String description;
	private String longitude;
	private String latitude;
	private String startDate;
	private String StartTime;
	private String endDate;
	private String endTime;
	
	/***
	 * get event id
	 * @return eid event_id
	 */
	public String getEid() {
		return eid;
	}

	/***
	 * set event id
	 * @param eid event_id
	 */
	public void setEid(String eid) {
		this.eid = eid;
	}

	/***
	 * get place
	 * @return place
	 */
	public String getPlace() {
		return place;
	}

	/***
	 * set event name
	 * @param ename event_name
	 */
	public void setEname(String ename) {
		this.place = ename;
	}

	/***
	 * get user id
	 * @return uid
	 */
	public String getUid() {
		return uid;
	}

	/***
	 * set user id
	 * @param uid user_id
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/***
	 * get type
	 * @return type String
	 */
	public String getType() {
		return type;
	}

	/***
	 * set type
	 * @param type String
	 */
	
	public void setType(String type) {
		this.type = type;
	}

	/***
	 * get longitude
	 * @return long longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/***
	 * set longitude
	 * @param long longitude
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/***
	 * get latitude
	 * @return latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/***
	 * set latitude
	 * @param latitude Latitude
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/***
	 * get start date
	 * @return date startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/***
	 * set start date
	 * @param date startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/***
	 * get start time
	 * @return time startTime
	 */
	public String getStartTime() {
		return StartTime;
	}

	/***
	 * set start time
	 * @param time startTime
	 */
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	/***
	 * get end date
	 * @return end_date
	 */
	public String getEndDate() {
		return endDate;
	}
	/***
	 * set end date
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/***
	 * get end time
	 * @return endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/***
	 * set end time
	 * @param endTime
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/***
	 * get description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
	/***
	 * set description
	 * @param description string
	 */
	public void setDescription(String description) {
		this.description = description;
	}
 
	public void print() {
		System.out.println(eid + "," + place);
	}
}
