package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.helper.DBHelper;
import com.polimi.childcare.shared.entities.Gita;
import com.polimi.childcare.shared.entities.PianoViaggi;
import com.polimi.childcare.shared.entities.Tappa;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GitaRequestHandlerSet extends GenericDaoSetEntityRequestHandler<SetEntityRequest<Gita>, Gita>
{
    @Override
    protected Class<Gita> getQueryClass() {
        return Gita.class;
    }
}
