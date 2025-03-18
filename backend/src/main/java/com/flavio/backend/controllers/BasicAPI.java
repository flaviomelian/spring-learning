package com.flavio.backend.controllers;

import com.flavio.backend.models.Person;
import com.flavio.backend.models.User;
import com.flavio.backend.repositories.PersonRepository;
import com.flavio.backend.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index() {
        StringBuilder html = new StringBuilder();
        html.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/styles.css\">");
        html.append("<h1><b>Iniciar Sesión</b></h1>");
        html.append("<div class=\"container\">");
        html.append("<form action=\"/api/people\" method=\"get\">"); // Cambiado a POST
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"user\">Usuario:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"user\" name=\"user\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"passwd\">Contraseña:</label>");
        html.append("<input type=\"password\" class=\"form-control\" id=\"passwd\" name=\"passwd\" required>");
        html.append("</div>");
        html.append("<button type=\"submit\" class=\"btn btn-primary\">Iniciar sesión</button>");
        html.append("</form>");
        html.append("</div>");
        return html.toString();
    }

    @GetMapping("/people")
    public String people(@RequestParam String user, @RequestParam String passwd, HttpSession session) {

        // Verificar las credenciales 
        User foundUser = userRepository.findByName(user);
        if (foundUser == null || !foundUser.getPassword().equals(passwd)) 
            return "redirect:/api/"; // Redirigir al inicio de sesión si las credenciales son incorrectas
    
        // Guardar las credenciales en la sesión
        session.setAttribute("user", user); // Store user in session
        session.setAttribute("passwd", passwd);
        
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
            html.append("<td>")
                .append("<button class='btn btn-warning update' onclick='fillUpdateForm(")
                .append(persona.getId()).append(", \"")
                .append(persona.getDNI()).append("\", \"")
                .append(persona.getName()).append("\", \"")
                .append(persona.getSurnames()).append("\", ")
                .append(persona.getAge()).append(", \"")
                .append(persona.getEmail()).append("\")'>Actualizar</button>")
                .append("<button class='btn btn-danger delete' onclick='deletePerson(").append(persona.getId()).append(")'>Eliminar</button>")
                .append("</td>");
            html.append("</tr>");
        }

        html.append("</table><form action=\"/api/add-person\" method=\"get\"><button class='btn btn-primary w-100' onclick='window.location.href=\"/api/add-person\"'>Agregar</button></form></div>");
        html.append("<script>")
            .append("function deletePerson(id) {")
            .append("  fetch('/api/person/' + id, { method: 'DELETE' })")
            .append("    .then(response => { if (response.ok) { location.reload(); } })")
            .append("    .catch(error => console.error('Error:', error));")
            .append("}")
            .append("function fillUpdateForm(id, dni, name, surnames, age, email) {")
            .append("  const url = `/api/update-person?id=${id}&dni=${dni}&name=${name}&surnames=${surnames}&age=${age}&email=${email}`;")
            .append("  window.location.href = url;")
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
        html.append("<h1><b>Agregar Persona</b></h1>");
        html.append("<div class=\"container\">");
        html.append("<form action=\"/api/add-person-to-database\" method=\"post\">");
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
        return html.toString();
    }

    @PostMapping("/add-person-to-database")
    public RedirectView addPerson(@RequestParam String dni, @RequestParam String name, @RequestParam String surnames, @RequestParam int age, @RequestParam String email, HttpSession session) {
        Person person = new Person();
        person.setDNI(dni);
        person.setName(name);
        person.setSurnames(surnames);
        person.setAge(age);
        person.setEmail(email);
        personRepository.save(person);

        // Recuperar las credenciales desde la sesión
        String user = (String) session.getAttribute("user");
        String passwd = (String) session.getAttribute("passwd");
        return new RedirectView("/api/people?user=" + user + "&passwd=" + passwd); // Redirigir a la lista de personas después de guardar
    }

    @GetMapping("/update-person")
    public String updatePerson(@RequestParam Integer id, @RequestParam String dni, @RequestParam String name, 
                            @RequestParam String surnames, @RequestParam int age, @RequestParam String email) {
        StringBuilder html = new StringBuilder();
        html.append("<link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css\">");
        html.append("<link rel=\"stylesheet\" href=\"/styles.css\">");
        html.append("<h1><b>Editar Persona</b></h1>");
        html.append("<div class=\"container\">");
        html.append("<form id=\"updateForm\" action=\"/api/update-person\" method=\"post\">"); // Corregido el método
        html.append("<input type=\"hidden\" id=\"id\" name=\"id\" value=\"").append(id).append("\">");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"dni\">DNI:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"dni\" name=\"dni\" value=\"").append(dni).append("\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"name\">Nombre:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" value=\"").append(name).append("\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"surnames\">Apellidos:</label>");
        html.append("<input type=\"text\" class=\"form-control\" id=\"surnames\" name=\"surnames\" value=\"").append(surnames).append("\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"age\">Edad:</label>");
        html.append("<input type=\"number\" class=\"form-control\" id=\"age\" name=\"age\" value=\"").append(age).append("\" required>");
        html.append("</div>");
        html.append("<div class=\"form-group\">");
        html.append("<label for=\"email\">Correo:</label>");
        html.append("<input type=\"email\" class=\"form-control\" id=\"email\" name=\"email\" value=\"").append(email).append("\" required>");
        html.append("</div>");
        html.append("<button type=\"submit\" class=\"btn btn-warning\">Guardar Cambios</button>");
        html.append("</form>");
        html.append("</div>");
        return html.toString();
    }


    @PostMapping("/update-person") // Cambiado de @PutMapping a @PostMapping para que funcione con formularios HTML
    @Transactional
    public RedirectView update(@RequestParam Integer id, 
                            @RequestParam String dni, 
                            @RequestParam String name, 
                            @RequestParam String surnames, 
                            @RequestParam int age, 
                            @RequestParam String email, HttpSession session) {
        Person person = personRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid person ID: " + id));

        person.setDNI(dni);
        person.setName(name);
        person.setSurnames(surnames);
        person.setAge(age);
        person.setEmail(email);

        personRepository.save(person); // Guarda la entidad actualizada en la base de datos

        // Recuperar las credenciales desde la sesión
        String user = (String) session.getAttribute("user");
        String passwd = (String) session.getAttribute("passwd");

        return new RedirectView("/api/people?user=" + user + "&passwd=" + passwd); // Redirigir a la lista de personas después de guardar
    }
    
}
