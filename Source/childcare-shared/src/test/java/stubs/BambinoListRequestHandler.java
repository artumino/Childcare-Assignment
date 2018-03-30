package stubs;

import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

public class BambinoListRequestHandler implements IRequestHandler<BambinoListRequestStub> {
    @Override
    public BaseResponse processRequest(BambinoListRequestStub request)
    {
        if(request != null)
            return new BambinoListResponseStub(((BambinoListRequestStub)request).getCode(), ((BambinoListRequestStub)request).getPayload());

        return null;
    }
}
