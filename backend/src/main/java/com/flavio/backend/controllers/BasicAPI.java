package com.flavio.backend.controllers;

import com.flavio.backend.models.Person;
import com.flavio.backend.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BasicAPI {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping("/people")
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
            html.append("<td>").append("<button class='btn btn-warning update' onclick='window.location.href=\"/api/update-person\"'>Actualizar</button>");
            html.append("<button class='btn btn-danger delete' onclick='deletePerson(" + persona.getId() + ")'>Eliminar</button>").append("</td>");
            html.append("</tr>");
        }

        html.append("</table><button class='btn btn-primary w-100' onclick='window.location.href=\"/api/add-person\"'>Agregar</button></div>");
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

    @GetMapping("/add-person")
    public String addPerson() {
        StringBuilder html = new StringBuilder();
        html.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/styles.css\">");
        html.append("<h1 style=\"\r\n" + //
                        "    position: relative;\r\n" + //
                        "    right: 6%;\r\n" + //
                        "\"><b>Agregar Persona</b></h1>");
        html.append("<div class=\"container\">");
        html.append("<form action=\"/api/add-person\" method=\"post\">");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"dni\">DNI:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"dni\" name=\"dni\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"name\">Nombre:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"surnames\">Apellidos:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"surnames\" name=\"surnames\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"age\">Edad:</label>");
        html.append("<input type=\"number\" class=\"form-control\" id=\"age\" name=\"age\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"email\">Correo:</label>");
        html.append("<input type=\"email\" class=\"form-control\" id=\"email\" name=\"email\" required>");
        html.append("</div>");
        html.append("<button type=\"submit\" class=\"btn btn-primary\">Guardar</button>");
        html.append("</form>");
        html.append("</div>");
        personRepository.save(new Person());
        return html.toString();
    }

    @PostMapping("/add-person")
    public RedirectView addPerson(@RequestParam String dni, @RequestParam String name, @RequestParam String surnames, @RequestParam int age, @RequestParam String email) {
        Person person = new Person();
        person.setDNI(dni);
        person.setName(name);
        person.setSurnames(surnames);
        person.setAge(age);
        person.setEmail(email);
        personRepository.save(person);
        return new RedirectView("/api/people"); // Redirigir a la lista de personas después de guardar
    }

    @GetMapping("/update-person")
    public String updatePerson() {
        StringBuilder html = new StringBuilder();
        html.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/styles.css\">");
        html.append("<h1 style=\"\r\n" + //
                        "    position: relative;\r\n" + //
                        "    right: 6%;\r\n" + //
                        "\"><b>Editar Persona</b></h1>");
        html.append("<div class=\"container\">");
        html.append("<form action=\"/api/people\" method=\"get\">");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"dni\">DNI:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"dni\" name=\"dni\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"name\">Nombre:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"surnames\">Apellidos:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"surnames\" name=\"surnames\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"age\">Edad:</label>");
        html.append("<input type=\"number\" class=\"form-control\" id=\"age\" name=\"age\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"email\">Correo:</label>");
        html.append("<input type=\"email\" class=\"form-control\" id=\"email\" name=\"email\" required>");
        html.append("</div>");
        html.append("<button type=\"submit\" class=\"btn btn-primary\">Guardar Cambios</button>");
        html.append("</form>");
        html.append("</div>");
        html.append("<script>")
            .append("function updatePerson(id, name, surnames, age, email) {")
            .append("  fetch('/api/person/' + id, { method: 'PUT' })")
            .append("    .then(response => { if (response.ok) { location.reload(); } })")
            .append("    .catch(error => console.error('Error:', error));")
            .append("}")
            .append("</script>");
        return html.toString();
    }
}
