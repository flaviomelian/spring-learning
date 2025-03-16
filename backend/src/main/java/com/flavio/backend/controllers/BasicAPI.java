package com.flavio.backend.controllers;

import com.flavio.backend.models.Person;
import com.flavio.backend.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BasicAPI {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/")
    public String people() {
        List<Person> personas = personRepository.findAll();

        // Construir la tabla HTML con los datos
        StringBuilder html = new StringBuilder();
        html.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/styles.css\">"); 
        html.append("<h1><b>Personas</b></h1>");
        html.append("<div class=\"container\"><table border='1'>");
        html.append("<tr><th>ID</th><th>DNI</th><th>Nombre</th><th>Apellido</th><th>Edad</th><th>Correo</th><th>Acciones</th></tr>");

        for (Person persona : personas) {
            html.append("<tr>");
            html.append("<td>").append(persona.getId()).append("</td>");
            html.append("<td>").append(persona.getDNI()).append("</td>");
            html.append("<td>").append(persona.getName()).append("</td>");
            html.append("<td>").append(persona.getSurnames()).append("</td>");
            html.append("<td>").append(persona.getAge()).append("</td>");
            html.append("<td>").append(persona.getEmail()).append("</td>");
            html.append("<td>").append("<button class='btn btn-warning update'>Actualizar</button>");
            html.append("<button class='btn btn-danger delete' onclick='deletePerson(" + persona.getId() + ")'>Eliminar</button>").append("</td>");
            html.append("</tr>");
        }

        html.append("</table><button class='btn btn-primary w-100'>Agregar</btn></div>");
        html.append("<script>")
            .append("function deletePerson(id) {")
            .append("  fetch('/api/person/' + id, { method: 'DELETE' })")
            .append("    .then(response => { if (response.ok) { location.reload(); } })")
            .append("    .catch(error => console.error('Error:', error));")
            .append("}")
            .append("</script>");
        return html.toString();
    }

    @DeleteMapping("/person/{id}")
    public void deletePerson(@PathVariable Integer id) {
        personRepository.deleteById(id);
    }
}
