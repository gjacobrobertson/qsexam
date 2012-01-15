package qsexam

class Sales implements Comparable {
	int week
	int year
	int storeoh
	int salesu
	int intransit
	float sales
	Store store
	
	static mapping = {
		table: 'sales'
		version false
		id column: 'sales_id', sqlType: 'int'
		sales sqlType: 'decimal'
		store sqlType: 'int'
	}
	
	int compareTo(obj) {
		year.compareTo(obj.year) ? year.compareTo(obj.year) : week.compareTo(obj.week)
	}
}
