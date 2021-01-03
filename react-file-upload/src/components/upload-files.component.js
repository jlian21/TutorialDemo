import React, { Component } from "react";
import UploadService from "../services/upload-files.service";

export default class UploadFiles extends Component {
    constructor(props) {
        super(props);
    
        this.state = {
          selectedFiles: undefined,
          currentFile: undefined,
          progress: 0,
          message: "",
    
          fileInfos: [],
        };

        this.selectFile = this.selectFile.bind(this);
        this.upload = this.upload.bind(this);
        this.componentDidMount = this.componentDidMount.bind(this);
    }

    selectFile(event) {
        this.setState({
          selectedFiles: event.target.files,
        });
    }

    upload() {
        // ??? how does the loop word here
        let currentFile = this.state.selectedFiles[0];
    
        this.setState({
          progress: 0,
          currentFile: currentFile,
        });
    
        UploadService.upload(currentFile, (event) => {
          this.setState({
            progress: Math.round((100 * event.loaded) / event.total),
          });
        })
          .then((response) => {
            this.setState({
              message: response.data.message,
            });
            return UploadService.getFiles();
          })
          .then((files) => {
            this.setState({
              fileInfos: files.data,
            });
          })
          .then(console.log(files[0].url))
          .catch(() => {
            this.setState({
              progress: 0,
              message: "Could not upload the file!",
              currentFile: undefined,
            });
          });
    
        this.setState({
          selectedFiles: undefined,
        });
    }


    // ???
    componentDidMount() {
        UploadService.getFiles().then((response) => {
          this.setState({
            fileInfos: response.data,
          });
        });
    }
    

    render() {
        const {
          selectedFiles,
          currentFile,
          progress,
          message,
          fileInfos,
        } = this.state;
    
        return (
          <div>
            {/* progress bar */}
            {currentFile && (
              <div className="progress">
                <div
                  className="progress-bar progress-bar-info progress-bar-striped"
                  role="progressbar"
                  aria-valuenow={progress}
                  aria-valuemin="0"
                  aria-valuemax="100"
                  style={{ width: progress + "%" }}
                >
                  {progress}%
                </div>
              </div>
            )}

            {/* choose file button */}
            <label className="btn btn-default">
              <input type="file" onChange={this.selectFile} />
            </label>

            {/* upload button */}
            <button className="btn btn-success"
              disabled={!selectedFiles}
              onClick={this.upload}
            >
              Upload
            </button>

            {/* upload message div */}
            <div className="alert alert-light" role="alert">
              {message}
            </div>

            {/* list of files div */}
            <div className="card">
              <div className="card-header">List of Files</div>
              <ul className="list-group list-group-flush">
                {fileInfos &&
                  fileInfos.map((file, index) => (
                    <li className="list-group-item" key={index}>
                      <a href={file.url}>{file.id}</a>
                      <script type="text/javascript">console.log("????")</script>
                    </li>
                  ))}
              </ul>
            </div>
          </div>
        );
    }
}