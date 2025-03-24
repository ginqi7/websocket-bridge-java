;;; websocket-bridge-java-demo.el ---                -*- lexical-binding: t; -*-

;; Copyright (C) 2025  Qiqi Jin

;; Author: Qiqi Jin <ginqi7@gmail.com>
;; Keywords:

;; This program is free software; you can redistribute it and/or modify
;; it under the terms of the GNU General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU General Public License for more details.

;; You should have received a copy of the GNU General Public License
;; along with this program.  If not, see <https://www.gnu.org/licenses/>.

;;; Commentary:

;;

;;; Code:

(defvar java-jar-path
  (expand-file-name
   "../../../target/websocket-bridge-java-1.0-SNAPSHOT-jar-with-dependencies.jar"))

(defun java-demo-start ()
  "Start websocket bridge real-time-translation."
  (interactive)
  (websocket-bridge-app-start "java-demo"
                              "java -jar"
                              java-jar-path))



(defun java-demo-restart ()
  "Restart websocket bridge real-time-translation and show process."
  (interactive)
  (websocket-bridge-app-exit "java-demo")
  (java-demo-start)
  (websocket-bridge-app-open-buffer "java-demo"))

(defun java-demo-plist-message (&rest arg)
  (let ((name (plist-get arg :name))
        (id (plist-get arg :id)))
    (print (format "id %s, name %s" name id))))

(websocket-bridge-call "java-demo" "message" "Hello1")

(websocket-bridge-call "java-demo" "value" "Hello")

(websocket-bridge-call "java-demo" "plist" "Hello")

(websocket-bridge-call "java-demo")

(provide 'websocket-bridge-java-demo)
;;; websocket-bridge-java-demo.el ends here
