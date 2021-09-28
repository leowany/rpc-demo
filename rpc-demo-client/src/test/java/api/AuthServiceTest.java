package api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leo.rpc.Hello;
import com.leo.rpc.HelloService;
import com.leo.rpc.NettyClientMain;
import com.leo.rpc.WorldService;
import com.leo.rpc.annotation.RpcReference;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { NettyClientMain.class })
public class AuthServiceTest {

	@RpcReference
	private HelloService helloService;

	@RpcReference
	private WorldService worldService;

	@Test
	public void test() throws InterruptedException {
		for (int i = 0; i < 1000; i++) {
			System.out.println(helloService.hello(new Hello("111", "222")));
			System.out.println(worldService.world(new Hello("111", "222")));
		}
		Thread.currentThread().sleep(1000000);
	}

}
