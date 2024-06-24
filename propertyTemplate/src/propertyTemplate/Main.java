package propertyTemplate;

import propertyTemplate.common.PropertyLoader;

public class Main {

	public static void main(String[] args) {

		System.out.println("----------Process started ------------");
		try {
			if (args.length == 0 || args[0] == null || args[0].isEmpty()) {
				System.err.println(
						"ERROR: Property File Name is not given. Please provide info -Dproperty.file=<Path of property file>");
				System.exit(-1);
			}

			String sPropertyFile = args[0];
			PropertyLoader.propertyFilePath = sPropertyFile;

			System.out.println(sPropertyFile);

			String abc = PropertyLoader.gerProperty("abc");

			System.out.println("abc's value : " + abc);

			System.out.println("----------Process finished ------------");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
