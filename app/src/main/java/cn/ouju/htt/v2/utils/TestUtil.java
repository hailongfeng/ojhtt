/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this imageFile except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package cn.ouju.htt.v2.utils;

/**仅测试用，图片地址可能会失效
 * @author Lemon
 */
public class TestUtil {



	/**
	 * 图片地址，仅供测试用
	 */
	public static String[] URLS = {
		"http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000",
		"http://common.cnblogs.com/images/icon_weibo_24.png",
		"http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000",
		"http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000",
		"http://common.cnblogs.com/images/wechat.png",
		"https://static.oschina.net/uploads/user/1344/2689427_200.jpg?t=1484029839000",
		"http://static.oschina.net/uploads/user/998/1997902_50.jpg?t=1407806577000",
		"http://static.oschina.net/uploads/user/1/3064_50.jpg?t=1449566001000",
		"http://static.oschina.net/uploads/user/51/102723_50.jpg?t=1449212504000",
		"http://static.oschina.net/uploads/user/48/96331_50.jpg",
		"http://static.oschina.net/uploads/user/48/97721_50.jpg?t=1451544779000",
		"http://static.oschina.net/uploads/user/48/96289_50.jpg?t=1452751699000",
		"http://static.oschina.net/uploads/user/19/39085_50.jpg",
		"http://static.oschina.net/uploads/user/1332/2664107_50.jpg?t=1457405500000",
		"http://static.oschina.net/uploads/user/1385/2770216_50.jpg?t=1464405516000",
		"http://static.oschina.net/uploads/user/427/855532_50.jpg?t=1435030876000",
		"http://static.oschina.net/uploads/user/629/1258821_50.jpg?t=1378063141000",
		"http://static.oschina.net/uploads/user/1200/2400261_50.png?t=1439638750000",
		"https://static.oschina.net/uploads/user/1143/2286031_50.jpg?t=1468900004000"
	};

	public static String getPicture(int index) {
		return index < 0 || index >= URLS.length ? null : URLS[index];
	}

}
