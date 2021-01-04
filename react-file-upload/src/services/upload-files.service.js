import http from "../http-common";

class UploadFilesService {
  upload(file, onUploadProgress) {
    let formData = new FormData();

    formData.append("file", file);

    return http.post("/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress,
    });
  }

  deleteById(id) {
    console.log("deleted through service.js");
    return http.delete("/files/" + id);
  }

  getFiles() {
    return http.get("/files");
  }
}

export default new UploadFilesService();