package stubs;

import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.BaseRequest;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class BambinoListRequestStub extends BaseRequest implements Serializable
{
    private int code;
    private List<Bambino> payload;

    public BambinoListRequestStub(int code, List<Bambino> payload)
    {
        this.code = code;
        this.payload = payload;
    }

    public int getCode() { return code; }
    public List<Bambino> getPayload() { return payload != null ? Collections.unmodifiableList(payload): null; }
}
