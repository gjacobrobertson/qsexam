package org.qsexam

class Store {
	String name
	String address
	String contact_email
	SortedSet sales
	
	static hasMany = [sales:Sales]
	
	static mapping = {
		table 'store'
		version false
		id column: 'store_id', sqlType: 'int'
		sales: sqlType: 'int'	
	}
}
