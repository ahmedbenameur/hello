import xml.etree.ElementTree as ET
import html
import re
import os
# Charger et parser le fichier XML
tree = ET.parse("appDefinition.xml")
root = tree.getroot()
# Dossiers de sortie
output_dir = "extracted_code"
java_dir = os.path.join(output_dir, "java")
sql_dir = os.path.join(output_dir, "sql")
js_dir = os.path.join(output_dir, "js")
# Création des dossiers si non existants
os.makedirs(java_dir, exist_ok=True)
os.makedirs(sql_dir, exist_ok=True)
os.makedirs(js_dir, exist_ok=True)
# Expressions régulières pour détecter les langages
java_pattern = re.compile(r"\b(import java|public class|void main|System\.out\.println)\b")
sql_pattern = re.compile(r"\b(SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|TRUNCATE|FROM|WHERE)\b", re.IGNORECASE)
js_pattern = re.compile(r"\b(function|console\.log|let |var |const |document\.)\b")
# Fonction pour nettoyer et formater le code
def format_code(code):
    """ Nettoie et formate le code pour qu'il soit bien structuré. """
    code = html.unescape(code)  # Décoder les caractères HTML
    code = re.sub(r'^\{"script":"|"\}$', '', code).strip()  # Supprimer les délimiteurs JSON
    code = code.replace("\\r\\n", "\n").replace("\\r", "\n").replace("\\n", "\n")  # Nettoyer les retours de ligne
    lines = [line.rstrip() for line in code.split("\n") if line.strip()]  # Supprimer les lignes vides
    # Ajouter indentation propre pour Java et JavaScript
    formatted_code = []
    indent_level = 0
    for line in lines:
        if line.strip().startswith("}") and indent_level > 0:
            indent_level -= 1
        formatted_code.append("    " * indent_level + line)
        if line.strip().endswith("{"):
            indent_level += 1
    return "\n".join(formatted_code) + "\n\n"
# Fonction pour extraire le nom de la classe Java
def extract_java_class_name(code):
    """ Extrait le nom de la classe Java. """
    match = re.search(r"\bclass\s+(\w+)", code)
    return match.group(1) if match else "UnknownClass"
# Fonction pour enregistrer le fichier avec un nom unique
def save_file(directory, base_name, extension, content):
    """ Sauvegarde un fichier avec un nom unique en évitant les collisions. """
    counter = 1
    file_name = f"{base_name}.{extension}"
    file_path = os.path.join(directory, file_name)
    while os.path.exists(file_path):  # Éviter d'écraser un fichier existant
        file_name = f"{base_name}_{counter}.{extension}"
        file_path = os.path.join(directory, file_name)
        counter += 1
    with open(file_path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f":coche_blanche: Fichier généré : {file_path}")
# Parcourir les éléments XML pour extraire les scripts
for elem in root.iter():
    if elem.tag == "pluginProperties":
        raw_code = elem.text
        if raw_code:
            cleaned_code = format_code(raw_code)
            # Vérifier le type de code et l'enregistrer dans un fichier distinct
            if java_pattern.search(cleaned_code):
                class_name = extract_java_class_name(cleaned_code)
                save_file(java_dir, class_name, "java", cleaned_code)
            elif sql_pattern.search(cleaned_code):
                save_file(sql_dir, f"query_{len(os.listdir(sql_dir)) + 1}", "sql", cleaned_code)
            elif js_pattern.search(cleaned_code):
                save_file(js_dir, f"script_{len(os.listdir(js_dir)) + 1}", "js", cleaned_code)
print("\n:tada: Extraction et formatage terminés ! Codes enregistrés dans le dossier 'extracted_code'.")
