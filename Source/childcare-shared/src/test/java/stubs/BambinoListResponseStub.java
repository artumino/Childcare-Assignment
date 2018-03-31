package stubs;

import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.responses.ListResponse;

import java.util.ArrayList;
import java.util.List;

public class BambinoListResponseStub extends ListResponse<Bambino>
{
    public BambinoListResponseStub(int code, List<Bambino> payload)
    {
        super(code, payload);
    }
}
