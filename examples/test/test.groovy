if (this.args.length > 0) {
	for (arg in this.args) {
		f = new File(arg)
		println("${f.name}\t-> exits: ${f.exists()}, canRead: ${f.canRead()} --> `${f.absoluteFile}`")
	}
} else {
	println("Usage: groovy ${this.class.getName()} filename")
}