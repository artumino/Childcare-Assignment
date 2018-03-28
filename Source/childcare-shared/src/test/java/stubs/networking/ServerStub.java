package stubs.networking;

import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import stubs.BambinoListRequestStub;
import stubs.BambinoListResponseStub;

public class ServerStub
{
    public static BaseResponse messageReceived(BaseRequest request)
    {
        if(request instanceof BambinoListRequestStub)
        {
            //Echo back data
            return new BambinoListResponseStub(((BambinoListRequestStub)request).getCode(), ((BambinoListRequestStub)request).getPayload());
        }
        return null;
    }
}
