$(document).ready(function() {
   document.getElementById('uploadForm').addEventListener('submit', function(event) {
               event.preventDefault();

               const formData = new FormData();
               const fileInput = document.getElementById('fileInput');
               formData.append('file', fileInput.files[0]);
			   
			   // 顯示轉圈圖標
               document.getElementById('spinner').style.display = 'block';
               document.getElementById('uploadStatus').textContent = '';

               fetch('/files/upload', {
                   method: 'POST',
                   body: formData
               })
               .then(response => {
				 // 隱藏轉圈圖標
                 document.getElementById('spinner').style.display = 'none';

               // Add logging to see the status code
               console.log('Response status:', response.status);
               if (!response.ok) {
                   if (response.status === 413) {
                       throw new Error('File too large');
                   } else {
                       throw new Error('Upload failed');
                   }
               }
                return response.json();
           })
               .then(responseData => {
                   const downloadUrl = responseData.downloadUrl;
   				const shortendownloadUrl = responseData.shortendownloadUrl;
                   const linkContainer = document.getElementById('linkContainer');
//                   const downloadLink = document.getElementById('downloadLink');
                   const downloadLinkShort = document.getElementById('downloadLinkShort');
                   downloadLinkShort.href = downloadUrl;
//                   downloadLink.href = downloadUrl;
                   // 更新文字內容為下載網址
//                   downloadLink.textContent = downloadUrl;
   				downloadLinkShort.textContent = shortendownloadUrl;

                   linkContainer.style.display = 'block';
               })
               .catch(error => {
                   console.error('Error:', error);
				   // 隱藏轉圈圖標並顯示錯誤
                   document.getElementById('spinner').style.display = 'none';
                   document.getElementById('uploadStatus').textContent = error.message;
               if (error.message === 'File too large') {
                   alert('File too large. Please select a smaller file.');
               } else {
                   alert('File upload failed. Please try again.');
               }
               });
           });
});
