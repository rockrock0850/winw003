package com.ebizprise.winw.project.enums;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.SimpleScheduleBuilder;

public enum ScheduleEnum {
	NOW(0) {
		public SimpleScheduleBuilder setCycle(int interval, int repeatCount) {
			return SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow();
		}
	},
	SECOND(1) {
		public SimpleScheduleBuilder setCycle(int interval, int repeatCount) {
			return SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(interval).repeatForever();
		}
	},
	MINUTE(2) {
		public SimpleScheduleBuilder setCycle(int interval, int repeatCount) {
			return SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(interval).repeatForever();
		}
	},
	HOUR(3) {
		public SimpleScheduleBuilder setCycle(int interval, int repeatCount) {
			return SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(interval).repeatForever();
		}
	},
	DAY(4) {
		public CalendarIntervalScheduleBuilder setCycle(int interval, int repeatCount) {
			return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInDays(interval);
		}
	},
	WEEK(5) {
		public CalendarIntervalScheduleBuilder setCycle(int interval, int repeatCount) {
			return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInWeeks(interval);
		}
	},
	MONTH(6) {
		public CalendarIntervalScheduleBuilder setCycle(int interval, int repeatCount) {
			return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(interval);
		}
	},
	YEAR(7) {
		public CalendarIntervalScheduleBuilder setCycle(int interval, int repeatCount) {
			return CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInYears(interval);
		}
	};

	@SuppressWarnings("rawtypes")
    public abstract ScheduleBuilder setCycle(int interval, int repeatCount);

	public int timeUnit;

	public int getTimeUnit() {
		return timeUnit;
	}

	ScheduleEnum(int timeUnit) {
		this.timeUnit = timeUnit;
	}

	public static ScheduleEnum getTimeUnit(int timeUnit) {
		for (ScheduleEnum scheduleEnum : ScheduleEnum.values()) {
			if (scheduleEnum.getTimeUnit() == timeUnit) {
				return scheduleEnum;
			}
		}
		return null;
	}
}
