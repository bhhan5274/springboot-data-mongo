package com.bhhan.mongo.file.web;

import com.bhhan.mongo.file.model.Video;
import com.bhhan.mongo.file.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by hbh5274@gmail.com on 2020-09-27
 * Github : http://github.com/bhhan5274
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;

    @GetMapping("/{id}")
    public String getVideo(@PathVariable String id, Model model) throws IOException {
        Video video = videoService.getVideo(id);
        model.addAttribute("title", video.getTitle());
        model.addAttribute("url", "/videos/stream/" + id);

        return "videos";
    }

    @GetMapping("/stream/{id}")
    public void streamVideo(@PathVariable String id, HttpServletResponse response) throws IOException {
        Video video = videoService.getVideo(id);
        FileCopyUtils.copy(video.getStream(), response.getOutputStream());
    }

    @GetMapping("/upload")
    public String uploadVideo(Model model){
        model.addAttribute("message", "hello");
        return "uploadVideo";
    }

    @PostMapping("/add")
    public String addVideo(@RequestParam String title, @RequestParam MultipartFile file, Model model) throws IOException {
        String id = videoService.addVideo(title, file);
        return "redirect:/videos/" + id;
    }
}
