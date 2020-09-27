package com.bhhan.mongo.file.web;

import com.bhhan.mongo.file.model.Photo;
import com.bhhan.mongo.file.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Controller
@RequestMapping("/photos")
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/{id}")
    public String getPhoto(@PathVariable String id, Model model){
        Photo photo = photoService.getPhoto(id);
        model.addAttribute("title", photo.getTitle());
        model.addAttribute("image", Base64.getEncoder().encodeToString(photo.getImage().getData()));
        return "photos";
    }

    @GetMapping("/upload")
    public String uploadPhoto(Model model){
        model.addAttribute("message", "hello");
        return "uploadPhoto";
    }

    @PostMapping("/add")
    public String addPhoto(@RequestParam String title, @RequestParam MultipartFile image, Model model) throws IOException {
        String id = photoService.addPhoto(title, image);
        return "redirect:/photos/" + id;
    }
}
