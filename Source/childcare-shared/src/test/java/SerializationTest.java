import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.SerializableListResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import org.junit.Test;
import org.junit.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

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
    public void SerializeDeserializePayloadedClassesTest()
    {
        Random rnd = new Random();

        ArrayList<Serializable> payload = new ArrayList<>();
        for(int i = 0; i < 20; i++)
            payload.add(rnd.nextInt());

        SerializableListResponse response = new SerializableListResponse(200, payload);
        byte[] serArr = SerializationUtils.serializeToByteArray(response);
        Integer received = SerializationUtils.deserializeByteArray(serArr, Integer.class);
        Assert.assertNull(received);

        SerializableListResponse dResponse = SerializationUtils.deserializeByteArray(serArr, SerializableListResponse.class);
        Assert.assertNotNull(dResponse);
        Assert.assertEquals(response.getCode(), dResponse.getCode());
        Assert.assertEquals(dResponse.getPayload().size(), payload.size());

        for(int i = 0; i < payload.size(); i++)
            Assert.assertTrue(dResponse.getPayload().get(i).equals(payload.get(i)));
    }

    //Controllo che almeno le classi con solo tipi complessi Serializable possano essere serializzate
    @Test
    public void SerializeDeserializePayloadedDowncastedClassesTest()
    {
        Random rnd = new Random();

        ArrayList<Serializable> payload = new ArrayList<>();
        for(int i = 0; i < 20; i++)
            payload.add(rnd.nextInt());

        SerializableListResponse response = new SerializableListResponse(200, payload);
        byte[] serArr = SerializationUtils.serializeToByteArray(response);
        Integer received = SerializationUtils.deserializeByteArray(serArr, Integer.class);
        Assert.assertNull(received);

        BaseResponse dResponse = SerializationUtils.deserializeByteArray(serArr, BaseResponse.class);
        Assert.assertNotNull(dResponse);
        Assert.assertEquals(response.getCode(), dResponse.getCode());

        SerializableListResponse sResponse = (SerializableListResponse)dResponse;
        Assert.assertEquals(sResponse.getPayload().size(), payload.size());

        for(int i = 0; i < payload.size(); i++)
            Assert.assertTrue(sResponse.getPayload().get(i).equals(payload.get(i)));
    }
}
