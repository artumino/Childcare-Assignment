import com.polimi.childcare.shared.networking.responses.ArrayListResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class SerializationTest
{

    //Controllo che almeno le classi con solo tipi primitivi possano essere serializzate
    @Test
    public void SerializeDeserializeBaseClassesTest()
    {
        BaseResponse response = new BaseResponse(200);
        byte[] serArr = SerializationUtils.serializeToByteArray(response);
        BaseResponse received = SerializationUtils.deserializeByteArray(serArr, BaseResponse.class);
        Assert.assertNotNull(received);
        Assert.assertEquals(response.getCode(), received.getCode());
    }

    //Controllo che almeno le classi con solo tipi complessi Serializable possano essere serializzate
    @Test
    @Ignore
    public void SerializeDeserializePayloadedClassesTest()
    {
        //FIXME: Indagare serializzazione
        ArrayListResponse response = new ArrayListResponse(200, new ArrayList<Integer>() {{
            add(1);
            add(10);
            add(32405);
        }});
        byte[] serArr = SerializationUtils.serializeToByteArray(response);
        BaseResponse received = SerializationUtils.deserializeByteArray(serArr, BaseResponse.class);
        Assert.assertNull(received);

        ArrayListResponse dResponse = SerializationUtils.deserializeByteArray(serArr, ArrayListResponse.class);
        Assert.assertNotNull(dResponse);
        Assert.assertEquals(response.getCode(), dResponse.getCode());
        Assert.assertEquals(dResponse.getPayload().size(), 3);
        Assert.assertTrue(dResponse.getPayload().get(0).equals(1));
        Assert.assertTrue(dResponse.getPayload().get(1).equals(10));
        Assert.assertTrue(dResponse.getPayload().get(2).equals(32405));
    }
}
