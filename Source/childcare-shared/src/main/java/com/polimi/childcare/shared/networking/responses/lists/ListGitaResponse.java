package com.polimi.childcare.shared.networking.responses.lists;

import com.polimi.childcare.shared.entities.Gita;
import java.util.List;

public class ListGitaResponse extends ListResponse<Gita>
{
    public ListGitaResponse(int code, List<Gita> payload) {
        super(code, payload);
    }
}
