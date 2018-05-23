package com.polimi.childcare.client.android.viewholders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;
import com.polimi.childcare.client.android.ImageStore;
import com.polimi.childcare.client.android.R;
import com.polimi.childcare.client.android.viewholders.annotations.LayoutAnnotation;
import com.polimi.childcare.shared.entities.Bambino;

import java.util.UUID;

@LayoutAnnotation(LayoutID = R.layout.item_presenza)
public class BambinoViewHolder extends GenericViewHolder<Bambino>
{
    public static final String ACTION_TAKE_PICTURE = "BambinoViewHolder.TakePicture";
    public static final String EXTRA_UUID = "uuid";

    private Bambino linkedBambino;

    private AppCompatImageView imgPreviewBambino;
    private TextView txtNomeCompleto;
    private TextView txtStato;
    private TextView txtCodiceFiscane;
    private TextView txtMatricola;


    public BambinoViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bindView(Bambino element)
    {
        this.linkedBambino = element;

        this.imgPreviewBambino = itemView.findViewById(R.id.imgBambino);
        this.txtNomeCompleto = itemView.findViewById(R.id.txtNomeCognome);
        this.txtStato = itemView.findViewById(R.id.txtStato);
        this.txtCodiceFiscane = itemView.findViewById(R.id.txtCodiceFiscale);
        this.txtMatricola = itemView.findViewById(R.id.txtMatricola);

        this.imgPreviewBambino.setOnClickListener((view) -> {
            Intent takePictureIntent = new Intent(ACTION_TAKE_PICTURE);
            takePictureIntent.putExtra(EXTRA_UUID, UUID.nameUUIDFromBytes(String.valueOf(linkedBambino.getID()).getBytes()).toString());
            LocalBroadcastManager.getInstance(itemView.getContext()).sendBroadcast(takePictureIntent);
        });

        updateView();
    }

    @Override
    public void updateView()
    {
        if(this.linkedBambino != null)
        {
            this.txtNomeCompleto.setText(String.format("%s %s",
                    linkedBambino.getNome(),
                    linkedBambino.getCognome()));
            this.txtCodiceFiscane.setText(linkedBambino.getCodiceFiscale());
            this.txtMatricola.setText(String.valueOf(linkedBambino.getID()));

            Bitmap loadedImage = ImageStore.getInstance().GetImage(itemView.getContext(), UUID.nameUUIDFromBytes(String.valueOf(linkedBambino.getID()).getBytes()));
            if(loadedImage != null)
                this.imgPreviewBambino.setImageBitmap(loadedImage);
            else
                this.imgPreviewBambino.setImageDrawable(itemView.getContext().getDrawable(R.drawable.ic_supervisor_account_black_24dp));

            //TODO: Presenza
        }
    }
}
