package Sunrise;

import com.tuya.sunrise.client.service.inner.production.IDeviceAuthorizeSearchService;

/**
 * @author baopc@tuya.com
 * @date 2020/1/19
 */
public class SunriseClient {

    private IDeviceAuthorizeSearchService client;

    public static void main(String[] args){
        IDeviceAuthorizeSearchService client = new IDeviceAuthorizeSearchService
        client.getAuthBySnAndStep();
    }
}
