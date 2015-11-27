package com.iweipeng.utility;

public class AppParams {

	public enum ConvertStatus {
		NO(0), YES(1);

		private int value;

		ConvertStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}
}
