package org.example.wallet.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.example.wallet.dto.LoginRequest;
import org.example.wallet.dto.LoginResponse;
import org.example.wallet.model.*;
import org.example.wallet.utils.JwtUtils;

@Path("/nasabah")
public class NasabahResource {

    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response register(Nasabah nasabah) {
        if (Nasabah.find("email", nasabah.email).firstResultOptional().isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email sudah terdaftar").build();
        }

        nasabah.persist();

        Saldo saldo = new Saldo();
        saldo.nasabah = nasabah;
        saldo.persist();

        return Response.status(Response.Status.CREATED).entity(nasabah).build();
    }

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response login(LoginRequest loginRequest) {
        Nasabah nasabah = Nasabah.find("email", loginRequest.getEmail()).firstResult();

        if (nasabah != null && nasabah.password.equals(loginRequest.getPassword())) {
            LoginAttempts loginAttempts = new LoginAttempts();
            loginAttempts.email = loginRequest.getEmail();
            loginAttempts.is_successful = true;
            loginAttempts.persist();

            String token = generateJwtToken(nasabah);
            return Response.ok(new LoginResponse(token)).build();
        } else {
            LoginAttempts loginAttempts = new LoginAttempts();
            loginAttempts.email = loginRequest.getEmail();
            loginAttempts.is_successful = false;
            loginAttempts.persist();

            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

    private String generateJwtToken(Nasabah nasabah) {
        return JwtUtils.generateJwtToken(nasabah);
    }

    @GET
    @Path("/saldo")
    @Produces("application/json")
    @Authenticated
    public Response cekSaldo(@HeaderParam("Authorization") String authorizationHeader) {
        System.out.println("authorizationHeader : "+ authorizationHeader);
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token Bearer tidak ditemukan atau tidak valid")
                    .build();
        }

        String token = authorizationHeader.replace("Bearer ", "");

        try {
            Long nasabahId = JwtUtils.getNasabahIdFromToken(token);

            Saldo saldo = Saldo.find("nasabah.id", nasabahId).firstResult();

            if (saldo != null) {
                return Response.ok(saldo).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Saldo tidak ditemukan").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token tidak valid atau ID Nasabah tidak ditemukan").build();
        }
    }

    @POST
    @Path("/logout")
    @Produces("application/json")
    public Response logout() {
        return Response.ok().build();
    }

    @GET
    @Path("/layanan")
    @Produces("application/json")
    public Response getLayanan() {
        return Response.ok(Layanan.listAll()).build();
    }

    @POST
    @Path("/transaksi")
    @Consumes("application/json")
    @Produces("application/json")
    @Transactional
    public Response transaksi(Transaksi transaksi) {
        // Proses transaksi (misalnya cek saldo)
        Saldo saldo = Saldo.findById(transaksi.nasabah.id);
        if (saldo == null || saldo.saldo < transaksi.jumlah) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Saldo tidak cukup").build();
        }

        saldo.saldo -= transaksi.jumlah;
        saldo.persist();

        transaksi.saldo_awal = saldo.saldo + transaksi.jumlah;
        transaksi.saldo_akhir = saldo.saldo;
        transaksi.persist();

        return Response.status(Response.Status.CREATED).entity(transaksi).build();
    }
}
