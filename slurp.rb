in_file = File.open("fields.txt", "r")
out_file = File.open("resources/public/fields.json", "w")

is_title = false
first = true
out = '[{"title":'
in_file.readlines.each do |line|
    line = line.strip!
    if is_title
        out = out [0..-2]
        out += ']},{"title":'
    end
    out += '"' + line + '",' if line.length > 2
    if is_title or first
        out += '"focuses": ['
        first = false
    end
    is_title = line.length < 2
end
out = out[0..-2] + ']}]'
out_file.puts out 
