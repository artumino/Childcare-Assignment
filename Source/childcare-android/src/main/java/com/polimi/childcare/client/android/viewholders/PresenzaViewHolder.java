package com.polimi.childcare.client.android.viewholders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;
import com.polimi.childcare.client.android.ImageStore;
import com.polimi.childcare.client.android.R;
import com.polimi.childcare.client.android.tuples.BambinoGruppoTuple;
import com.polimi.childcare.client.android.viewholders.annotations.LayoutAnnotation;
import com.polimi.childcare.shared.entities.RegistroPresenze;

import java.util.UUID;

@LayoutAnnotation(LayoutID = R.layout.item_presenza)
public class PresenzaViewHolder extends GenericViewHolder<BambinoGruppoTuple>
{
    public static final String ACTION_TAKE_PICTURE = "PresenzaViewHolder.TakePicture";
    public static final String ACTION_CHANGE_PRESENZA = "PresenzaViewHolder.ChangePresenza";
    public static final String EXTRA_UUID = "uuid";
    public static final String EXTRA_TUPLE_HASHCODE = "tuple_hashcode";

    private BambinoGruppoTuple linkedTuple;

    private AppCompatImageView imgPreviewBambino;
    private TextView txtNomeCompleto;
    private TextView txtStato;
    private TextView txtCodiceFiscane;
    private TextView txtMatricola;


    public PresenzaViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindView(BambinoGruppoTuple element)
    {
        this.linkedTuple = element;

        this.imgPreviewBambino = itemView.findViewById(R.id.imgBambino);
        this.txtNomeCompleto = itemView.findViewById(R.id.txtNomeCognome);
        this.txtStato = itemView.findViewById(R.id.txtStato);
        this.txtCodiceFiscane = itemView.findViewById(R.id.txtCodiceFiscale);
        this.txtMatricola = itemView.findViewById(R.id.txtMatricola);

        this.imgPreviewBambino.setOnClickListener((view) -> {
            Intent takePictureIntent = new Intent(ACTION_TAKE_PICTURE);
            takePictureIntent.putExtra(EXTRA_UUID, UUID.nameUUIDFromBytes(String.valueOf(linkedTuple.getLinkedBambino().getID()).getBytes()).toString());
            LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(takePictureIntent);
        });

        this.txtStato.setOnClickListener((view) -> {
            Intent changePresenzaIntent = new Intent(ACTION_CHANGE_PRESENZA);
            changePresenzaIntent.putExtra(EXTRA_TUPLE_HASHCODE, this.linkedTuple.hashCode());
            LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(changePresenzaIntent);
        });

        updateView();
    }

    @Override
    public void updateView()
    {
        if(this.linkedTuple != null)
        {
            this.txtNomeCompleto.setText(String.format("%s %s",
                    linkedTuple.getLinkedBambino().getNome(),
                    linkedTuple.getLinkedBambino().getCognome()));
            this.txtCodiceFiscane.setText(linkedTuple.getLinkedBambino().getCodiceFiscale());
            this.txtMatricola.setText(String.valueOf(linkedTuple.getLinkedBambino().getID()));

            Bitmap loadedImage = ImageStore.getInstance().GetImage(itemView.getContext(), UUID.nameUUIDFromBytes(String.valueOf(linkedTuple.getLinkedBambino().getID()).getBytes()));
            if(loadedImage != null)
                this.imgPreviewBambino.setImageBitmap(loadedImage);
            else
                this.imgPreviewBambino.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_supervisor_account_black_24dp));

            this.txtStato.setText(linkedTuple.getLinkedPresenza().getStato().toString());
            this.txtStato.setTextColor(0xFFFFFFFF);
            switch (linkedTuple.getLinkedPresenza().getStato())
            {
                case Presente:
                    txtStato.setBackgroundColor(0xFF00FF00);
                    break;
                case PresenteMezzoErrato:
                    txtStato.setBackgroundColor(0xFFff8c00);
                    break;
                case EntratoInRitardo:
                    txtStato.setBackgroundColor(0xFFFFFF00);
                    break;
                case UscitoInAnticipo:
                    txtStato.setBackgroundColor(0xFF0000FF);
                    break;
                case Uscito:
                    txtStato.setBackgroundColor(0xFFFF3333);
                    break;
                default:
                    txtStato.setBackgroundColor(0xFFFF0000);
                break;
            }
        }
    }
}
