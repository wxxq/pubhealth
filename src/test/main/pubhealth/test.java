package pubhealth;

import org.junit.Test;
import org.springframework.util.Assert;

/*
 * @author melo
*/
public class test {
	
	@Test
	public void match(){
		String databaseName = "pubhealth";
		boolean a = databaseName .matches("[\\w-]+");
		System.out.println(a);
		Assert.isTrue(databaseName .matches("[\\w-]+"),
				"Database name must only contain letters, numbers, underscores and dashes!");
	}
}


