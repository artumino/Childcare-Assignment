package com.polimi.childcare.server.handlers.entities.setters;

import com.polimi.childcare.server.database.DatabaseSession;
import com.polimi.childcare.server.networking.IRequestHandler;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Contatto;
import com.polimi.childcare.shared.entities.Genitore;
import com.polimi.childcare.shared.entities.Pediatra;
import com.polimi.childcare.shared.networking.requests.setters.SetEntityRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;

import java.util.Set;

public class SetBambino implements IRequestHandler<SetEntityRequest<Bambino>>
{
    @Override
    public BaseResponse processRequest(SetEntityRequest<Bambino> request)
    {
        final BaseResponse[] response = new BaseResponse[1];
        Throwable exception = DatabaseSession.getInstance().execute((session) -> {

                Set<Contatto> contattiset = request.getEntity().getContatti();
                Bambino bambinoget = session.getByID(Bambino.class, request.getEntity().getID(), true);

                if (request.getOldHashCode() == bambinoget.consistecyHashCode()) {
                    //Aggiorna le relazioni
                    if (!request.isToDelete()) {

                        Set<Genitore> genitoreset = request.getEntity().getGenitori();
                        Pediatra pediatraGet = session.getByID(Pediatra.class, bambinoget.getPediatra().getID());
                        request.getEntity().setPediatra(pediatraGet);

                        for (Genitore unupdated : genitoreset) {
                            request.getEntity().removeGenitore(unupdated);
                            Genitore genitoreGet = session.getByID(Genitore.class, unupdated.getID());
                            if (genitoreGet != null)
                                request.getEntity().addGenitore(genitoreGet);
                        }

                        for (Contatto unupdated : contattiset) {
                            request.getEntity().unsafeRemoveContatto(unupdated);
                            Contatto updated = session.getByID(Contatto.class, unupdated.getID());

                            if (updated != null) {
                                updated.removeBambino(bambinoget);
                                session.update(updated);
                                request.getEntity().unsafeAddContatto(updated);
                            }

                        }
                    }
                    else
                    {
                        //Rimuove le relazioni di cui non è owner
                        for (Contatto unupdated : contattiset)
                        {
                            request.getEntity().unsafeRemoveContatto(unupdated);
                            Contatto updated = session.getByID(Contatto.class, unupdated.getID());
                            if (updated != null)
                            {
                                updated.removeBambino(bambinoget);
                                session.update(updated);
                            }
                        }
                    }
                }


            response[0] = SetGenericEntity.Setter(request, Bambino.class, session);

            return !(response[0] instanceof BadRequestResponse);
        });

        if(exception != null)
            return new BadRequestResponse.BadRequestResponseWithMessage(exception.getMessage());

        return response[0];
    }
}