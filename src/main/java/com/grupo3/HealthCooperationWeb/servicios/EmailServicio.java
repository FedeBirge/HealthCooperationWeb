package com.grupo3.HealthCooperationWeb.servicios;

import com.grupo3.HealthCooperationWeb.excepciones.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailServicio {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String nombre, String apellido, String email,
            String telefono, String esp, String msj) throws MyException {
        SimpleMailMessage message = new SimpleMailMessage();
        validar(nombre, apellido, email, telefono, esp, msj);
        message.setFrom(email);
        message.setTo("healtheggcooperationgroup@gmail.com");
        message.setSubject(nombre + " " + apellido + " " + " " + email + " " + telefono + " " + esp);
        message.setText(msj);
        mailSender.send(message); // método Send(envio), propio de Java Mail Sender.
    }

    protected void validar(String nombre, String apellido, String email,
            String telefono, String esp, String mje)
            throws MyException {
        if (nombre == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar su nombre");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new MyException("Debe ingresar su apellido");
        }

        if (email == null || email.isEmpty()) {
            throw new MyException("Debe ingresar un email");
        }

        if (telefono == null || nombre.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (esp == null || esp.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }
        if (mje == null || mje.isEmpty()) {
            throw new MyException("Debe ingresar un nombre");
        }

    }
}
